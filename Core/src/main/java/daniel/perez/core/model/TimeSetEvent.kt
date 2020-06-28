package daniel.perez.core.model

data class TimeSetEvent(val hour: Int,
                        val minute: Int,
                        val half: Half)

enum class Half { AM, PM }