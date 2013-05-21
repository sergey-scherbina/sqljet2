name := "sqljet2"

organization := "org.tmatesoft"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"

fork in test := true

javaOptions in test += "-XX:CompileThreshold=100"

parallelExecution in test := false
