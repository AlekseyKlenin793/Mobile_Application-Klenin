package ru.mirea.kleninaa.employee_db;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etName, etSalary;
    private TextView tvEmployees;
    private EmployeeDao employeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etSalary = findViewById(R.id.etSalary);
        tvEmployees = findViewById(R.id.tvEmployees);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnLoad = findViewById(R.id.btnLoad);

        employeeDao = App.getInstance().getDatabase().employeeDao();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                int salary = Integer.parseInt(etSalary.getText().toString());
                Employee employee = new Employee();
                employee.name = name;
                employee.salary = salary;
                employeeDao.insert(employee);
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Employee> employees = employeeDao.getAll();
                StringBuilder employeesStr = new StringBuilder();
                for (Employee employee : employees) {
                    employeesStr.append(employee.name).append(" - ").append(employee.salary).append("\n");
                }
                tvEmployees.setText(employeesStr.toString());
            }
        });
    }
}