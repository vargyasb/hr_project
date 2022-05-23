package hu.webuni.hr.vargyasb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HRConfigProperties {

	private Salary salary = new Salary();

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	public static class Salary {
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
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setLimit(int percent) {
			this.percent = percent;
		}

	}

	public static class Smart {
		private Double limitLow;
		private Double limitMid;
		private Double limitHigh;
		private Integer percentLow;
		private Integer percentMid;
		private Integer percentHigh;

		public Double getLimitLow() {
			return limitLow;
		}

		public void setLimitLow(Double limitLow) {
			this.limitLow = limitLow;
		}

		public Double getLimitMid() {
			return limitMid;
		}

		public void setLimitMid(Double limitMid) {
			this.limitMid = limitMid;
		}

		public Double getLimitHigh() {
			return limitHigh;
		}

		public void setLimitHigh(Double limitHigh) {
			this.limitHigh = limitHigh;
		}

		public Integer getPercentLow() {
			return percentLow;
		}

		public void setPercentLow(Integer percentLow) {
			this.percentLow = percentLow;
		}

		public Integer getPercentMid() {
			return percentMid;
		}

		public void setPercentMid(Integer percentMid) {
			this.percentMid = percentMid;
		}

		public Integer getPercentHigh() {
			return percentHigh;
		}

		public void setPercentHigh(Integer percentHigh) {
			this.percentHigh = percentHigh;
		}

	}
}
