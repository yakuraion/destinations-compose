plugins {
    `maven-publish`
    signing
    java
}

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
}

signing {
    sign(publishing.publications["mavenJava"])
}
