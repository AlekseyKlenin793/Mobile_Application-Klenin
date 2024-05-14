package ru.mirea.kleninaa.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class MyDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Создание и возвращение нового диалогового окна выбора даты
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Обработка выбора даты
        // Здесь можно выполнить действия, связанные с выбранной датой, аналогично методу onTimeSet.
        // Пример вывода выбранной даты в лог:
        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // month начинается с 0, поэтому добавляем 1
        Log.d("MyDateDialog", "Выбранная дата: " + selectedDate);
    }
}