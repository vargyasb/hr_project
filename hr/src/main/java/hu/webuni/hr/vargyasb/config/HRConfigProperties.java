package hu.webuni.hr.vargyasb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HRConfigProperties {

	private Employee employee = new Employee();

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public static class Employee {
		private Default def = new Default();
		private Smart smart = new Smart();

		public Default getDef() {
			return def;
		}

		public void setDef(Default def) {
			this.def = def;
		}

		public Smart getSmart() {
			return smart;
		}

		public void setSmart(Smart smart) {
			this.smart = smart;
		}
	}

	public static class Default {
		private int limit;

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

	}

	public static class Smart {
		private int limitLow;
		private int limitMid;
		private int limitHigh;
		private int percentLow;
		private int percentMid;
		private int percentHigh;

		public int getLimitLow() {
			return limitLow;
		}

		public void setLimitLow(int limitLow) {
			this.limitLow = limitLow;
		}

		public int getLimitMid() {
			return limitMid;
		}

		public void setLimitMid(int limitMid) {
			this.limitMid = limitMid;
		}

		public int getLimitHigh() {
			return limitHigh;
		}

		public void setLimitHigh(int limitHigh) {
			this.limitHigh = limitHigh;
		}

		public int getPercentLow() {
			return percentLow;
		}

		public void setPercentLow(int percentLow) {
			this.percentLow = percentLow;
		}

		public int getPercentMid() {
			return percentMid;
		}

		public void setPercentMid(int percentMid) {
			this.percentMid = percentMid;
		}

		public int getPercentHigh() {
			return percentHigh;
		}

		public void setPercentHigh(int percentHigh) {
			this.percentHigh = percentHigh;
		}

	}
}
