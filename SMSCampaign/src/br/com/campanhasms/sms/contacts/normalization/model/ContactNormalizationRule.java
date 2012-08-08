package br.com.campanhasms.sms.contacts.normalization.model;

public class ContactNormalizationRule {

	private String matcherRegex;

	private String replacerRegex;
	public ContactNormalizationRule(String matcherRegex, String replacerRegex) {
		super();
		this.matcherRegex = matcherRegex;
		this.replacerRegex = replacerRegex;
	}

	public String getMatcherRegex() {
		return matcherRegex;
	}

	public String getReplacerRegex() {
		return replacerRegex;
	}

	public void setMatcherRegex(String matcherRegex) {
		this.matcherRegex = matcherRegex;
	}

	public void setReplacerRegex(String replacerRegex) {
		this.replacerRegex = replacerRegex;
	}

}
