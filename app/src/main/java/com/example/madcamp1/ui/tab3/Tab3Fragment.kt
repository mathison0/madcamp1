package com.example.madcamp1.ui.tab3

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.madcamp1.databinding.CalendarDayLayoutBinding
import com.example.madcamp1.databinding.FragmentTab3Binding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder

import java.time.LocalDate
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import org.json.JSONArray
import androidx.core.content.ContextCompat
import com.example.madcamp1.R



class Tab3Fragment : Fragment() {

    private var _binding: FragmentTab3Binding? = null
    private val binding get() = _binding!!

    // 날짜별 문제 푼 개수 (더미 데이터)
    private val solvedCounts = mutableMapOf<LocalDate, Int>()

//    // 시연용 더미 데이터
//    private val fakeSubmissionMap = mapOf(
//        LocalDate.now().minusDays(1) to 1,
//        LocalDate.now().minusDays(3) to 2,
//        LocalDate.now().minusDays(4) to 3,
//        LocalDate.now().minusDays(5) to 4
//    )

    // 현재 월
    private var currentMonth = YearMonth.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTab3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DebugTag", "onViewCreated")
        generateDummyData()

        val calendarView = binding.calendarView
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val textView = container.textView
                val date = data.date
                textView.text = date.dayOfMonth.toString()

                val count = solvedCounts[date] ?: 0
                val level = when {
                    count >= 4 -> 4
                    else -> count
                }

                val color = when (level) {
                    4 -> ContextCompat.getColor(requireContext(), R.color.color_chip4)
                    3 -> ContextCompat.getColor(requireContext(), R.color.color_chip3)
                    2 -> ContextCompat.getColor(requireContext(), R.color.color_chip2)
                    1 -> ContextCompat.getColor(requireContext(), R.color.color_chip1)
                    else -> Color.LTGRAY
                }

                // 💡 배경 drawable 가져와서 색만 바꾸기
                val background = textView.background.mutate() as GradientDrawable
                background.setColor(color)
            }
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                // 헤더 뷰에 대한 초기화 코드 작성 (예: 요일 텍스트 설정)
            }
        }
        calendarView.outDateStyle = OutDateStyle.EndOfGrid;

        val startMonth = currentMonth.minusMonths(100) // Adjust as needed
        val endMonth = currentMonth.plusMonths(100) // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        binding.monthTitleText.text = "${currentMonth.year}년 ${currentMonth.monthValue}월"
        calendarView.monthScrollListener = { month ->
            currentMonth = month.yearMonth
            binding.monthTitleText.text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월"
        }

        // 이전 달 버튼
        binding.btnPrevMonth.setOnClickListener {
            val prevMonth = currentMonth.minusMonths(1)
            calendarView.smoothScrollToMonth(prevMonth)
        }

        // 다음 달 버튼
        binding.btnNextMonth.setOnClickListener {
            val nextMonth = currentMonth.plusMonths(1)
            calendarView.smoothScrollToMonth(nextMonth)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateDummyData() {
        val prefs = requireContext().getSharedPreferences("checkbox_prefs", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("checked_date_list", "[]")
        val jsonArray = JSONArray(jsonString)

        solvedCounts.clear()
        for (i in 0 until jsonArray.length()) {
            val date = LocalDate.parse(jsonArray.getString(i))
            solvedCounts[date] = (solvedCounts[date] ?: 0) + 1
        }
//        // 시연용 데이터 추가
//        for ((date, count) in fakeSubmissionMap) {
//            solvedCounts[date] = (solvedCounts[date] ?: 0) + count
//        }
    }


    // Calendar class
    class DayViewContainer(view: View) : ViewContainer(view) {
        val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    }

    class MonthViewContainer(view: View) : ViewContainer(view) {
        // Alternatively, you can add an ID to the container layout and use findViewById()
        val titlesContainer = view as ViewGroup
    }
}