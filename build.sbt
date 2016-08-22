lazy val `no-idea` = project
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

libraryDependencies ++= Vector(
  Library.scalaTest % "test"
)

initialCommands := """|import huntfamily.id.au.no.idea._
                      |""".stripMargin
