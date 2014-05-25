package com.tuplejump.calliope.utils

import org.joda.time.DateTime
import java.util.Date
import java.util.{List => JList}
import scala.collection.JavaConverters._

import scala.language.implicitConversions

object ImplicitHelpers {

  implicit def Date2DateTime(date: Date): DateTime = if (date == null) null else new DateTime(date)

  implicit def DateTime2Date(date: DateTime): Date = if (date == null) null else new Date(date.getMillis)

  implicit def JList2List[T](list: JList[T]): List[T] = list.asScala.toList
}
