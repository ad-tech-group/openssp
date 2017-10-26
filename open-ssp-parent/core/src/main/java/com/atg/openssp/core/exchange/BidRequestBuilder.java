package com.atg.openssp.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;

import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Device;
import openrtb.bidrequest.model.Gender;
import openrtb.bidrequest.model.Geo;
import openrtb.bidrequest.model.Impression;
import openrtb.bidrequest.model.Site;
import openrtb.bidrequest.model.User;
import openrtb.bidrequest.model.Video;
import openrtb.tables.VideoBidResponseProtocol;

/**
 * RequestBuilder builds the BidRequest Object for RTB Exchange.
 * 
 * @author André Schmer
 *
 */
public class BidRequestBuilder {

	/**
	 * Build a request object regarding to the OpenRTB Specification.
	 * 
	 * @return {@see BidRequest}
	 */
	public static BidRequest build(final SessionAgent agent) {

		// TODO: fill with true data
		final BidRequest bidRequest = new BidRequest.Builder().setId("9876").setSite(new Site.Builder().setId("1234").setDomain("domain").addCat("1").addCat("3").setName(
		        "mySiteObject").setPage("page").build()).setDevice(new Device.Builder().setGeo(new Geo.Builder().setCity("Hamburg").setCountry("DEU").setLat(53.563452f).setLon(
		                9.925742f).setZip("22761").build()).build()).addImp(new Impression.Builder().setId("45").setVideo(new Video.Builder().addMime(
		                        "application/x-shockwave-flash").setH(400).setW(600).setMaxduration(100).setMinduration(30).addProtocol(VideoBidResponseProtocol.VAST_2_0
		                                .getValue()).setStartdelay(1).build()).build()).setUser(new User.Builder().setBuyeruid("HHcFrt-76Gh4aPl").setGender(Gender.MALE).setId("99")
		                                        .setYob(1981).build()).build();

		return bidRequest;
	}

}
