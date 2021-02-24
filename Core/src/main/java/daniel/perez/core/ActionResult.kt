package daniel.perez.core

sealed class ActionResult
{
    object Success: ActionResult()
    class Failure(val t: Throwable): ActionResult()
    object InTransit: ActionResult()
}