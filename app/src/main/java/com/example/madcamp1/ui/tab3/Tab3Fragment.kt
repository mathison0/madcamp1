package com.example.madcamp1.ui.tab3

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
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

class Tab3Fragment : Fragment() {

    private var _binding: FragmentTab3Binding? = null
    private val binding get() = _binding!!

    // 날짜별 문제 푼 개수 (더미 데이터)
    private val solvedCounts = mutableMapOf<LocalDate, Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTab3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    4 -> Color.parseColor("#33691E")
                    3 -> Color.parseColor("#558B2F")
                    2 -> Color.parseColor("#7CB342")
                    1 -> Color.parseColor("#AED581")
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
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100) // Adjust as needed
        val endMonth = currentMonth.plusMonths(100) // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        binding.monthTitleText.text = "${currentMonth.year}년 ${currentMonth.monthValue}월"
        calendarView.monthScrollListener = { month ->
            binding.monthTitleText.text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월"
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