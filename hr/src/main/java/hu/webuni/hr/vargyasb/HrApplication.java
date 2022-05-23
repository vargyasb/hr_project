package hu.webuni.hr.vargyasb;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.vargyasb.model.Employee;
import hu.webuni.hr.vargyasb.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner{

	@Autowired
	SalaryService salaryService;
	
	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Steve has a salary of: " + salaryService.calculateNewSalaryFor(new Employee(1L, "Steve", "Game Dev", 3000, LocalDateTime.of(2010, 10, 13, 10, 25))));
		System.out.println("Peter has a salary of:  " + salaryService.calculateNewSalaryFor(new Employee(2L, "Peter", "HR Associate", 2000, LocalDateTime.of(2015, 10, 13, 10, 25))));
		System.out.println("Anna has a salary of:  " + salaryService.calculateNewSalaryFor(new Employee(3L, "Anna", "Scrum Master", 2800, LocalDateTime.of(2020, 05, 10, 10, 25))));
		System.out.println("David has a salary of:  " + salaryService.calculateNewSalaryFor(new Employee(4L, "David", "Project Manager", 3200, LocalDateTime.of(2021, 10, 13, 10, 25))));
	}

}
