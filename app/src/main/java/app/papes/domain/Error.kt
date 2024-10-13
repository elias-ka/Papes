package app.papes.domain

sealed interface Error

sealed interface DataError : Error {
    enum class Network : Error {
        RateLimitExceeded,
        NotFound,
        Unknown
    }
}