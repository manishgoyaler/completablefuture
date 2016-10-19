package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.stackoverflow.LoadFromStackOverflowTask;
import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class S01_Introduction extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S01_Introduction.class);

	/**
	 * Broken abstraction - blocking method calls
	 */
	@Test
	public void blockingCall() throws Exception {
		final String title = client.mostRecentQuestionAbout("java");
		log.debug("Most recent Java question: '{}'", title);
	}


	@Test
	public void testmatchAny() throws Exception {
		final String title = client.mostRecentQuestionAbout("java");
		log.debug("Most recent Java question: '{}'", title);

		List<String> list1 = Arrays.asList("abc", "xyz", "lmn");

		List<MyClass> list2 = new ArrayList<MyClass>();

		MyClass obj = new MyClass("abc");
		list2.add(obj);
		obj = new MyClass("xyz");
		list2.add(obj);

		List<String> l3 =list1.stream().filter(x -> !list2.stream().anyMatch(y -> y.getStr().equalsIgnoreCase(x))).collect(Collectors.toList());
		l3.forEach(System.out::println);
		
	}
	static class MyClass {

		MyClass(String val)
		{
			this.str = val;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		String str;


	}

	@Test
	public void executorService() throws Exception {
		final Callable<String> task = () -> client.mostRecentQuestionAbout("java");
		final Future<String> javaQuestionFuture = executorService.submit(task);
		//...
		final String javaQuestion = javaQuestionFuture.get();
		log.debug("Found: '{}'", javaQuestion);
	}

	/**
	 * Composing is impossible
	 */
	@Test
	public void waitForFirstOrAll() throws Exception {
		final Future<String> java = findQuestionsAbout("java");
		final Future<String> scala = findQuestionsAbout("scala");

		//???
	}

	private Future<String> findQuestionsAbout(String tag) {
		final Callable<String> task = new LoadFromStackOverflowTask(client, tag);
		return executorService.submit(task);
	}

}

