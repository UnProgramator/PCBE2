package services;

import java.util.ArrayList;
import java.util.HashMap;

public class DomainService {
	
	private final HashMap<String, ArrayList<String>> subDomains;
	private final HashMap<String, String> parentDomain;
	
	public DomainService(String[][] domains) {
		parentDomain = new HashMap<String, String>();
		subDomains = new HashMap<String, ArrayList<String>>();
		for(String[] pair: domains) {
			parentDomain.put(pair[1], pair[0]);
			if(subDomains.containsKey(pair[0])) {
				subDomains.get(pair[0]).add(pair[1]);
			}
			else {
				subDomains.put(pair[0], new ArrayList<String>());
				subDomains.get(pair[0]).add(pair[1]);
			}
		}
	}
	
	public String getParent(String subdomain) {
		if(parentDomain.containsKey(subdomain))
			return parentDomain.get(subdomain);
		else
			return null;
	}
	
	public ArrayList<String> getSubdomains(String domain){
		if(subDomains.containsKey(domain))
			return subDomains.get(domain);
		else
			return null;
	}
	
	public ArrayList<String> getDomains(){
		return new ArrayList<String>(subDomains.keySet());
	}
	
	public ArrayList<String> getSubdomains(){
		return new ArrayList<String>(parentDomain.keySet());
	}
}
