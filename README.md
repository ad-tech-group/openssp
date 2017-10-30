## OpenSSP by ATG

### This is a Open-Source Supply-Side-Platform for general use.

This platform is also called a multi-channel-SSP which means that not only RTB is supported, also channels to a adserver and other SSP can be implemented.
The RTB Support is native implemented and need no extra definition.

This SSP is optimized for working and handle VideoAd-Impressions. Therefore there a objects with those characteristics.
But it can also be used for any other type of impressions such as banner etc.
Feel free to examine the code and adapt the behavior as you need.

Full support of the OpenRTB standard up to version 2.4. If a field or object from the protocol is missing, just add it.

There are some properties configuration files to configure some small, things to the SSP like URL, Credentials etc.
The Runtime Environment is based on a Tomcat.
The proprties files will be loaded at TC startup and a Watchdog is watching those files for changes. So the (most) values of this configurations are runtime sensitive.

Those file are:
- `global.runtime.xml`
- `local.runtime.xml`

### Elementary SSP functionality:
- A tag with a link to this SSP is placed on a website which contains the (Video-)Adplacement. This link contains a set of parameters (depends on your preferences). 
- SSP is called from this tag, the parameters are extrahated and validated (depends on your preferences).
- The SSP resolves the request data, e.g. the website or publisher, which is identified by an id (depends on your preferences) and loads the data to enrich from the cache. (For more information about the cache see section Data Caching)
- The SSP starts the ExchangeServer and the ExecutorService to call the demand service like DSP, SSP, Adserver or what ever you want and waits for the respond from the partner to start the auction.
- After the auction is finished, the winner is evaluated and a respond to the client can be prepared.

### Data Caching:

This OpenSSP uses some predefined data to enrich the incoming requests and building the various bidrequests.
Those data e.g. for Websites have to be loaded in a asynchronous and continous way.
The data will be loaded via a restful service normally cause of to call a database in realtime is not performantly possible, therefore this SSP loads in a periodically manner the data from a webservice and stores those data in a memory based key-value store, called the cache.

**Update v0.2:**
to increase the user experience and avoid a test implementation for set up an external data service to load the data from, some new example JSON based datastructures and its corresponding DataBroker are available. These structures can be extended to load the data to fill a valid and complete bidrequest. Additionally the payload of SSP request is reduced to only one parameter. 
Example: a call to OpenSSP with `?site=1` will map to an entry within `site_db.json` and loads its data from cache.

You can use it, improve it or do whatever you want with it.

Comments, proposals and improvements are welcome.  
For more informations contact André Schmer, schmer@ad-tech-group.com.  
October 2017
