package team.redrock.stickerset.main.utils

sealed interface FetchStatus {
    object NoFetched : FetchStatus
    object Fetching : FetchStatus
    object Fetched : FetchStatus
}