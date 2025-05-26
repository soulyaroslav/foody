package com.jerdoul.foody.domain.validation

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.ValidationError

abstract class ValidationProcessor {
    internal var next: ValidationProcessor? = null

    abstract fun validate(type: ValidationType): Result<Unit, ValidationError>

    protected fun validateNext(type: ValidationType): Result<Unit, ValidationError> =
        next?.validate(type = type) ?: Result.Success(Unit)

    companion object {
        fun initializeChain(head: ValidationProcessor, vararg chain: ValidationProcessor?): ValidationProcessor {
            var newHead: ValidationProcessor? = head
            for (nextInChain in chain) {
                newHead?.next = nextInChain
                newHead = nextInChain
            }
            return head
        }
    }
}






