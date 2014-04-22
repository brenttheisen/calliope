package com.tuplejump.calliope.macros

import com.tuplejump.calliope.Types.CQLRowMap
import scala.reflect.macros.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation


import scala.language.implicitConversions
import java.io.InvalidObjectException
import scala.collection.immutable.Stack
import scala.util.Try

trait Mappable[T] {
  def toCqlRow(t: T): CQLRowMap

  def fromCqlRow(map: CQLRowMap): T
}

trait Generator {
  def generate[T]: Mappable[T]
}

object MapperMacro {

  def withLowercaseMapper[T] = macro _generateWithLowercaseMapper[T]

  def withSnakecaseMapper[T] = macro _generateWithSnakecaseMapper[T]

  def withColumns[T](columns: String*): Mappable[T] = macro _generateWithColumnList[T]

  def withColumnMap[T](columnMap: Map[String, String]) = macro _generateWithColumnMap[T]

  def withMapper[T](columnMapper: (String, Int) => String) = macro _generateWithColumnMapper[T]


  def _generateWithColumnList[T: c.WeakTypeTag](c: Context)(columns: c.Expr[String]*): c.Expr[Mappable[T]] = {
    import c.universe._
    val tpe = c.weakTypeOf[T]

    ensureCaseClass(c)(tpe)

    val params = getParams(c)(tpe)

    if (!columns.isEmpty && (columns.length != params.length)) {
      c.abort(c.enclosingPosition, s"The case class [${tpe}}] expects ${params.length} values, you provided ${columns.length}")
    }

    val cols = columns.map(col => c.eval(c.Expr[String](c.resetAllAttrs(col.tree))))

    def colMapper: (String, Int) => String = {
      (s: String, i: Int) => cols(i)
    }

    generate[T, c.type](c)(tpe, params, colMapper)
  }

  def _generateWithColumnMapper[T: c.WeakTypeTag](c: Context)(columnMapper: c.Expr[(String, Int) => String]): c.Expr[Mappable[T]] = {
    import c.universe._
    val tpe = c.weakTypeOf[T]

    ensureCaseClass(c)(tpe)

    val params = getParams(c)(tpe)

    val colMapper: (String, Int) => String = c.eval(c.Expr[(String, Int) => String](c.resetAllAttrs(columnMapper.tree)))

    generate[T, c.type](c)(tpe, params, colMapper)
  }

  def _generateWithColumnMap[T: c.WeakTypeTag](c: Context)(columnMap: c.Expr[Map[String, String]]): c.Expr[Mappable[T]] = {
    import c.universe._
    val colMap: Map[String, String] = c.eval(c.Expr[Map[String, String]](c.resetAllAttrs(columnMap.tree)))
    val colMapper = reify {
      (s: String, i: Int) => colMap.getOrElse(s, s)
    }

    _generateWithColumnMapper[T](c)(colMapper)
  }

  def _generateWithLowercaseMapper[T: c.WeakTypeTag](c: Context): c.Expr[Mappable[T]] = {
    import c.universe._

    val colMapper = reify {
      (s: String, i: Int) => s.toLowerCase
    }
    _generateWithColumnMapper[T](c)(colMapper)
  }

  def _generateWithSnakecaseMapper[T: c.WeakTypeTag](c: Context): c.Expr[Mappable[T]] = {
    import c.universe._


    val colMapper = reify {
      (s: String, i: Int) => s.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase
    }

    _generateWithColumnMapper[T](c)(colMapper)
  }

  private def generate[T, C <: Context](c: C)(tpe: c.Type, params: List[c.universe.Symbol], mapperFunction: (String, Int) => String): c.Expr[Mappable[T]] = {
    import c.universe._

    val companion = tpe.typeSymbol.companionSymbol
    val (toMapParams, fromMapParams) = getMappers(c)(params, mapperFunction)

    c.Expr[Mappable[T]] { q"""
      new Mappable[$tpe] {
        import com.tuplejump.calliope.utils.RichByteBuffer._
        import com.tuplejump.calliope.Types.CQLRowMap

        def toCqlRow(t: $tpe):CQLRowMap = Map(..$toMapParams)
        def fromCqlRow(map: CQLRowMap) = $companion(..$fromMapParams)
      }
    """
    }
  }

  private def ensureCaseClass(c: Context)(tpe: c.Type) {
    val sym = tpe.typeSymbol
    if (!sym.isClass || !sym.asClass.isCaseClass)
      c.abort(c.enclosingPosition, s"$sym is not a case class")
  }

  private def getParams(c: Context)(tpe: c.Type) = {
    import c.universe._

    val declarations = tpe.declarations

    val ctor = declarations.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get

    ctor.paramss.head

  }


  private def getMappers(c: Context)(params: List[c.universe.Symbol], mapperFunction: (String, Int) => String) = {
    import c.universe._

    params.zipWithIndex.map {
      case (field: c.universe.Symbol, index: Int) =>
        val colName = mapperFunction(field.name.toString, index)
        val name = field.name.asInstanceOf[c.universe.TermName]

        (q"$colName -> t.$name", q"map($colName)")

    }.unzip
  }

  implicit def ss2sis(ss: String => String): (String, Int) => String = {
    (s: String, i: Int) => ss(s)
  }

  implicit def is2sis(is: String => String): (String, Int) => String = {
    (s: String, i: Int) => is(s)
  }


}
