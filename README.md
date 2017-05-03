## OPEN-SSP by ATG

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
- global.runtime.xml
- local.runtime.xml

### Elementary SSP functionality:
1. A tag with a link to this SSP is placed on a website which contains the (Video-)Adplacement. This link contains a set of parameters (depends on your preferences). 
2. SSP is called from this tag, the parameters are extrahated and validated (depends on your preferences).
3. The SSP resolves the request data, e.g. the webiste or publisher, which is identified by an id (depends on your preferences) and loads the data to enrich from the cache. (For more information about the cache see section Data Caching)
4. The SSP starts the ExchangeServer and the ExecutorService to call the demand service like DSP, SSP, Adserver or what ever you want and waits for the respond from the partner to start the auction.
5. After the auction is finished, the winner is evaluated and a respond to the client can be prepared.

### Data Caching:
This Open-SSP uses some predefined data to enrich the incoming requests and building the various bidrequests.
Those data e.g. for Websites have to be loaded in a asynchronous and continous way.
The data will be loaded via a restful service normally cause of to call a database in realtime is not performantly possible, therefore this SSP loads in a periodically manner the data from a webservice and stores those data in a memory based key-value store, called the cache.

You can use it, improve it or whatever you want with it.

Comments, proposals, improvements are welcome.
For more informations contact André Schmer, schmer@ad-tech-group.com.
May 2017

You can use the [editor on GitHub](https://github.com/ad-tech-group/openssp/edit/master/README.md) to maintain and preview the content for your website in Markdown files.

Whenever you commit to this repository, GitHub Pages will run [Jekyll](https://jekyllrb.com/) to rebuild the pages in your site, from the content in your Markdown files.

### Markdown

Markdown is a lightweight and easy-to-use syntax for styling your writing. It includes conventions for

```markdown
Syntax highlighted code block

# Header 1
## Header 2
### Header 3

- Bulleted
- List

1. Numbered
2. List

**Bold** and _Italic_ and `Code` text

[Link](url) and ![Image](src)
```

For more details see [GitHub Flavored Markdown](https://guides.github.com/features/mastering-markdown/).

### Jekyll Themes

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/ad-tech-group/openssp/settings). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://help.github.com/categories/github-pages-basics/) or [contact support](https://github.com/contact) and we’ll help you sort it out.
