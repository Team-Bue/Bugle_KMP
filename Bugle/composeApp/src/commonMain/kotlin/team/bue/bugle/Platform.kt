package team.bue.bugle

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform