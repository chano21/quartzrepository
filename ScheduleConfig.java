import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ScheduleConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	ApplicationContext appcontext;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	public SchedulerFactoryBean quartzScheduler() throws SchedulerException {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
	
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(appcontext);
		scheduler.setJobFactory(jobFactory);
		scheduler.setTransactionManager(transactionManager);
		scheduler.setOverwriteExistingJobs(true);
		scheduler.setSchedulerName("TestScueduler");
		
		Trigger[] trigger= {processeTrigger()1.getObject(),processeTrigger()2.getObject()};
		
		scheduler.setTriggers(trigger);
		scheduler.setSchedulerName("PCO-quartz-scheduler");
		scheduler.isAutoStartup();
		return scheduler;
	}
	
	
	
	@Bean
	public CronTriggerFactoryBean processTrigger1() {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(job1());
		cronTriggerFactoryBean.setCronExpression("0 0/1 * * * ? *");
		cronTriggerFactoryBean.setGroup("spring3-quartz");
		cronTriggerFactoryBean.setName("job1kkey");
		return cronTriggerFactoryBean;
	}
	
	@Bean
	public CronTriggerFactoryBean processTrigger2() {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(job2());
		cronTriggerFactoryBean.setCronExpression("0 0/1 * * * ? *");
		cronTriggerFactoryBean.setGroup("spring3-quartz");
		cronTriggerFactoryBean.setName("job2key");
		
		return cronTriggerFactoryBean;
	}
	@Bean
	public JobDetail job1() {
	     JobDetail job1 = JobBuilder.newJob(Test1.class)
	    		 		.withIdentity("job1")
	   		 			.build();
        return job1;
	}
	public JobDetail job2() {
	     JobDetail job1 = JobBuilder.newJob(Test2.class).withIdentity("job2").build();
       return job1;
	}

}
