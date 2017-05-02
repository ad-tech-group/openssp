package com.atg.openssp.common.provider;

import java.util.regex.Pattern;

/**
 * @author schmer
 *
 */
class MacroPattern {

	enum TYPE {
		PRICE(p_price), BID_ID(p_bid_id), IMP_ID(p_imp_id), SEAT_ID(p_seat_id), AD_ID(p_ad_id), CURRENCY(p_currency), AUCTION_ID(p_auction_id);
		private final Pattern p;

		private TYPE(final Pattern pattern) {
			p = pattern;
		}

		public Pattern pattern() {
			return p;
		}
	}

	private final static String AUCTION_PRICE = "\\$\\{AUCTION_PRICE\\}";
	private final static String AUCTION_ID = "\\$\\{AUCTION_ID\\}";
	private final static String AUCTION_BID_ID = "\\$\\{AUCTION_BID_ID\\}";
	private final static String AUCTION_IMP_ID = "\\$\\{AUCTION_IMP_ID\\}";
	private final static String AUCTION_SEAT_ID = "\\$\\{AUCTION_SEAT_ID\\}";
	private final static String AUCTION_AD_ID = "\\$\\{AUCTION_AD_ID\\}";
	private final static String AUCTION_CURRENCY = "\\$\\{AUCTION_CURRENCY\\}";

	private static Pattern p_price;
	private static Pattern p_bid_id;
	private static Pattern p_imp_id;
	private static Pattern p_seat_id;
	private static Pattern p_ad_id;
	private static Pattern p_currency;
	private static Pattern p_auction_id;

	static {
		p_price = Pattern.compile(AUCTION_PRICE);
		// p_price_enc = Pattern.compile(AUCTION_PRICE_ENC);
		p_bid_id = Pattern.compile(AUCTION_BID_ID);
		p_imp_id = Pattern.compile(AUCTION_IMP_ID);
		p_seat_id = Pattern.compile(AUCTION_SEAT_ID);
		p_ad_id = Pattern.compile(AUCTION_AD_ID);
		p_auction_id = Pattern.compile(AUCTION_ID);
		p_currency = Pattern.compile(AUCTION_CURRENCY);
	}

	static Pattern getPattern(final TYPE type) {
		return type.pattern();
	}
}
