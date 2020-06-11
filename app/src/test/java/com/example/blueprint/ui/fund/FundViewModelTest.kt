package com.example.blueprint.ui.fund

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.blueprint.repository.FundRepo
import com.example.blueprint.util.testObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FundViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockFundRepo: FundRepo = mockk()
    private lateinit var viewModel: FundViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = FundViewModel(mockFundRepo, Dispatchers.Main)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `checkFund emit correct fund amount`() = runBlockingTest {
        coEvery { mockFundRepo.checkAvailableFund() } returns 100

        viewModel.checkFund()

        val testObserver = viewModel.fundAmount.testObserver()
        assertThat(testObserver.observedValues.size).isEqualTo(1)
        assertThat(testObserver.observedValues[0]).isEqualTo(100)
    }

    @Test
    fun `fundAccepted emit current value`() = runBlockingTest {
        coEvery { mockFundRepo.addFund(any()) } returns 100

        viewModel.fundAccepted(400)

        val fundAmountObserver = viewModel.fundAmount.testObserver()
        val fundOfferAmountObserver = viewModel.fundOfferAmount.testObserver()
        assertThat(fundAmountObserver.observedValues.size).isEqualTo(1)
        assertThat(fundAmountObserver.observedValues[0]).isEqualTo(100)
        assertThat(fundOfferAmountObserver.observedValues.size).isEqualTo(1)
        assertThat(fundOfferAmountObserver.observedValues[0]).isNull()
        coVerify { mockFundRepo.addFund(eq(400)) }
    }
}
