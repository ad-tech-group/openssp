package com.atg.openssp.common.core.system.job;

import com.atg.openssp.common.jobservice.CommonJobTrigger;

/**
 * This class is useful to set a configuration for a job which can than be used by {@link CommonJobTrigger}.
 * 
 * @author André Schmer
 *
 */
public class JobConfig {

	private Class<? extends org.quartz.Job> jobClass;
	private String jobName;
	private String expression;

	public JobConfig() {}

	public static Builder newBuilder() {
		return new Builder();
	}

	public void setJobClass(final Class<? extends org.quartz.Job> clazz) {
		jobClass = clazz;
	}

	public void setExpression(final String expression) {
		this.expression = expression;
	}

	public void setJobName(final String jobname) {
		jobName = jobname;
	}

	public String getJobName() {
		return jobName;
	}

	public Class<? extends org.quartz.Job> getJobClazz() {
		return jobClass;
	}

	public String getExpression() {
		return expression;
	}

	public static class Builder {

		private final JobConfig config;

		public Builder() {
			config = new JobConfig();
		}

		public Builder setJobClass(final Class<? extends org.quartz.Job> clazz) {
			config.setJobClass(clazz);
			return this;
		}

		public Builder setJobName(final String jobname) {
			config.setJobName(jobname);
			return this;
		}

		public Builder setExpression(final String expression) {
			config.setExpression(expression);
			return this;
		}

		/**
		 * Creates the {@link JobConfig} This is a terminal Method.
		 * 
		 * @return JobConfig
		 */
		public JobConfig build() {
			return config;
		}
	}

}
