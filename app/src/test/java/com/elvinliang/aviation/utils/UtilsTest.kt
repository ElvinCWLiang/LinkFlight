package com.elvinliang.aviation.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class UtilsTest: FunSpec({

    test("double or zero") {
        forAll(
            row(1.1),
            row(null),
            row(2.0)
        ) {
            it.orZero() shouldBe (it ?: 0.0)
        }
    }

    test("string or unknown") {
        forAll(
            row(""),
            row("abc"),
            row("123456"),
            row(null)
        ) {
            it.orUnknown() shouldBe (it ?: "N/A")
        }
    }

    // TODO: mock MainViewModel
//    val accountService = Mockito.mock(AccountService::class.java)
//    val mainViewModel = LoginViewModel(accountService)
//    Mockito.`when`(mainViewModel.login("123456", "12345678", { route, popUp -> }))
//        .thenReturn()
})