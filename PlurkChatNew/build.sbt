import android.Keys._

import android.Dependencies.{apklib,aar}

android.Plugin.androidBuild
 
name := "PlurkChat"

version := "0.0.1"
 
scalaVersion := "2.10.4"

organization := "idv.funnybrain.plurkchat"

libraryDependencies ++= Seq(
  "com.actionbarsherlock" % "actionbarsherlock" % "4.4.0",
  "org.scribe" % "scribe" % "1.3.5"
)

platformTarget in Android := "android-19"

proguardScala := true

proguardScala in Android := false
 
run <<= run in Android
 
install <<= install in Android


