plugins {
    `maven-publish`
    signing
    java
}

val versionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

group = "io.github.yakuraion.destinationscompose"
version = versionCatalog.findVersion("destinations-compose").get().requiredVersion

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("$groupId:$artifactId")
                description.set(project.provider { project.description })
                url.set("https://github.com/yakuraion/destinations-compose")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    developer {
                        id.set("yakuraion")
                        name.set("Viachaslau Yakauleu")
                        email.set("yakuraion@gmail.com")
                        url.set("https://github.com/yakuraion")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/yakuraion/destinations-compose.git")
                    developerConnection.set("scm:git:ssh://github.com:yakuraion/destinations-compose.git")
                    url.set("https://github.com/yakuraion/destinations-compose/tree/main")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = project.property("ossrhUsername") as String?
                password = project.property("ossrhPassword") as String?
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
