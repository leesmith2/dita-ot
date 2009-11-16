package org.dita.dost.resolver;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
/**
 * URIResolverAdapter class, convert source into inputsteam.
 *
 */
public class URIResolverAdapter {
	private URIResolverAdapter() {
		// nop
	}

	public static InputSource convertToInputSource(Source source) {
		if(source==null){
			return null;
		}
		
		if (source instanceof SAXSource) {
			return ((SAXSource) source).getInputSource();
		} else if (source instanceof StreamSource) {
			StreamSource ss = (StreamSource) source;
			InputSource isource = new InputSource(ss.getInputStream());
			isource.setByteStream(ss.getInputStream());
			isource.setCharacterStream(ss.getReader());
			return isource;
		} else if (source instanceof DOMSource) {
			StringWriter writer = new StringWriter();
			try {
				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(writer));
				return new InputSource(new StringReader(writer.toString()));
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}
	public static InputStream convertTOInputStream(Source source){
		InputSource result=convertToInputSource(source);
		if(result==null){
			return null;
		}
		return result.getByteStream();
	}
}
