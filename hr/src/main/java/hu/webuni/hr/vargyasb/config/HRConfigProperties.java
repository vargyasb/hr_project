package hu.webuni.hr.vargyasb.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HRConfigProperties {

	private Salary salary = new Salary();
	
	private Jwt jwt = new Jwt();

	public Jwt getJwt() {
		return jwt;
	}

	public void setJwt(Jwt jwt) {
		this.jwt = jwt;
	}

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
	
	public static class Jwt {
	
		private String secret;
		private Duration expiry;
		private String issuer;
		private String alg;
		public String getSecret() {
			return secret;
		}
		public void setSecret(String secret) {
			this.secret = secret;
		}
		public Duration getExpiry() {
			return expiry;
		}
		public void setExpiry(Duration expiry) {
			this.expiry = expiry;
		}
		public String getIssuer() {
			return issuer;
		}
		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}
		public String getAlg() {
			return alg;
		}
		public void setAlg(String alg) {
			this.alg = alg;
		}
		
	}
	
	
}
