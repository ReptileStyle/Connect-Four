fun String?.capitalize(): String? {
    println("Before: $this")
        if(isNullOrBlank()) {
            println("After: $this")
            return this
        }
        if(length == 1) {
            println("After: ${uppercase()}")
            return uppercase()
        }
        else {
            println("After: ${this[0].uppercase() + substring(1)}")
            return this[0].uppercase() + substring(1)
        }
}