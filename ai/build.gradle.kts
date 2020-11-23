dependencies {
    val kotlinVersion: String by project
    implementation(platform(kotlin("bom", kotlinVersion)))
    implementation(kotlin("stdlib-jdk8"))

    val coroutinesVersion: String by project
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$coroutinesVersion"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

    val log4jVersion: String by project
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
}
