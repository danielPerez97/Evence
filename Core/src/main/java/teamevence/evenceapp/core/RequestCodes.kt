package teamevence.evenceapp.core

object RequestCodes
{
    const val REQUEST_SAF = 1

    fun map(code: Int): String
    {
        return when(code)
        {
            1 -> "REQUEST_SAF"
            else -> throw Exception("Unknown Request Code")
        }
    }
}