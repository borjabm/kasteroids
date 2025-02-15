package com.harper.asteroids

object TestUtil {
    
    fun readFile(path: String): String =
        this.javaClass.getResource(path)?.readText(Charsets.UTF_8)
            ?: error("Unable to read test resource from file: $path")
}