package com.temp0.core

/** The user's configured weight unit (Profile setting) — the single source of truth used
 *  everywhere a weight is displayed or entered (Builder, Exercise screen). */
enum class Units(val label: String) {
    KG("kg"),
    LB("lb"),
}
