package er.rest.format;

import net.sf.json.JSONSerializer;
import er.rest.ERXRestContext;
import er.rest.ERXRestRequestNode;
import er.rest.ERXRestUtils;

public class ERXJSONPRestWriter extends ERXJSONRestWriter {
	
	public void appendHeadersToResponse(ERXRestRequestNode node, IERXRestResponse response, ERXRestContext context) {
		response.setHeader("text/javascript;charset=UTF-8", "Content-Type");
	}

	public void appendToResponse(ERXRestRequestNode node, IERXRestResponse response, ERXRestFormat.Delegate delegate, ERXRestContext context) {
		
		// TODO this callback string needs to be properly set to include an optional 
		// method name in the response.
		
		String callbackName = (String) context.userInfoForKey("callback");
		
		if (callbackName != null)
			response.appendContentString(callbackName + "(");
		else 
			response.appendContentString("callback(");
		

		node = processNode(node);
		if (node != null) {
			node._removeRedundantTypes();
		}
		
		appendHeadersToResponse(node, response, context);
		Object object = node.toJavaCollection(delegate);
		if (object == null) {
			response.appendContentString("undefined");
		}
		else if (ERXRestUtils.isPrimitive(object)) {
			response.appendContentString(String.valueOf(object));
		}
		else {
			response.appendContentString(JSONSerializer.toJSON(object, configWithContext(context)).toString());
		}
		
		response.appendContentString(");");
	}

}
