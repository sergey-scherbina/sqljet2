name := "sqljet2"

organization := "org.tmatesoft"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"

fork in Test := true

javaOptions in Test += "-XX:CompileThreshold=1500"

parallelExecution in Test := false
