package com.tuplejump.calliope.macros

import scala.language.experimental.macros
import scala.reflect.macros.Context

import scala.language.implicitConversions
import com.datastax.driver.core.Row
import com.tuplejump.calliope.Types.CQLRowMap

trait Mappable[T] extends Serializable {
  implicit def toCqlRow(t: T): CQLRowMap

  implicit def fromNativeRow(row: Row): T
}


object Codec {

  def withLowercaseMapper[T] = macro _generateWithLowercaseMapper[T]

  def withSnakecaseMapper[T] = macro _generateWithSnakecaseMapper[T]

  def withColumns[T](columns: String*): Mappable[T] = macro _generateWithColumnList[T]

  def withColumnMap[T](columnMap: Map[String, String]) = macro _generateWithColumnMap[T]

  def withMapper[T](columnMapper: (String, Int) => String) = macro _generateWithColumnMapper[T]


  def _generateWithColumnList[T: c.WeakTypeTag](c: Context)(columns: c.Expr[String]*): c.Expr[Mappable[T]] = {

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

    val companion: Symbol = tpe.typeSymbol.companionSymbol
    val (toMapParams, fromNativeParams) = getMappers(c)(params, mapperFunction)

    c.Expr[Mappable[T]] { q"""
      import com.tuplejump.calliope.macros.Mappable
      import com.tuplejump.calliope.utils.RichByteBuffer._

      new Mappable[$tpe] {
        import com.tuplejump.calliope.Types.CQLRowMap
        import com.datastax.driver.core.Row

        implicit def toCqlRow(t: $tpe):CQLRowMap = Map(..$toMapParams)
        implicit def fromNativeRow(row: Row) = $companion(..$fromNativeParams)
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
      case (field: Symbol, index: Int) =>
        val colName = mapperFunction(field.name.toString, index)
        val name = field.name.asInstanceOf[TermName]
        val fieldType: Type = field.asTerm.typeSignature
        val fieldGetter = newTermName(getFieldGetter(c)(fieldType, colName))

        val toMap = q"$colName -> t.$name"
        //val fromMap= q"map($colName)"

        val fromRow = if (fieldType.takesTypeArgs) {
          val fieldTypeArgs = fieldType match {
            case TypeRef(_, _, args) => args
          }
          if (fieldTypeArgs.size == 1) {
            val typeArgClass = fieldTypeArgs.head.typeSymbol.asClass
            q"row.$fieldGetter($colName, $typeArgClass)"
          } else {
            val typeArgClass1 = fieldTypeArgs(0).typeSymbol.asClass
            val typeArgClass2 = fieldTypeArgs(1).typeSymbol.asClass
            q"row.$fieldGetter($colName, $typeArgClass1, $typeArgClass2)"
          }
        } else {
          q"row.$fieldGetter($colName)"
        }

        (toMap, fromRow)

    }.unzip
  }

  private def getFieldGetter(c: Context)(ft: c.type#Type, colName: String): String = {
    ft.typeSymbol.name.toString match {
      case "Boolean" =>
        "getBool"
      case "Int" | "Integer" =>
        "getInt"
      case "Long" =>
        "getLong"
      case "Date" =>
        "getDate"
      case "Float" =>
        "getFloat"
      case "Double" =>
        "getDouble"
      case "ByteBuffer" =>
        "getBytes"
      case "String" =>
        "getString"
      case "BigInteger" =>
        "getVarint"
      case "BigDecimal" =>
        "getDecimal"
      case "UUID" =>
        "getUUID"
      case "InetAddress" =>
        "getInet"
      case "List" =>
        "getList"
      case "Set" =>
        "getSet"
      case "Map" =>
        "getMap"
      case x =>
        println(x)
        "getBytesUnsafe"
    }
  }

  implicit def ss2sis(ss: String => String): (String, Int) => String = {
    (s: String, i: Int) => ss(s)
  }

  implicit def is2sis(is: String => String): (String, Int) => String = {
    (s: String, i: Int) => is(s)
  }
}
