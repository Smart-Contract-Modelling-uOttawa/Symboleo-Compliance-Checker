package it.unibo.ai.rec.model;

import it.unibo.ai.rec.common.CommonUtils;

import java.util.Hashtable;
import java.util.Map;

public class FluentsModel {
	private Map<String,FluentGroup> fluentGroups;
	private FluentGroupingStrategy strategy;
	
	
	
	public FluentsModel(FluentGroupingStrategy strategy) {
		fluentGroups = new Hashtable<String,FluentGroup>();
		this.strategy = strategy;
	}
	
	public FluentsModel() {
		this(new NoGroupingStrategy());
	}
	
	
	public void addMVI(String rawFluentName, MVI mvi) {
		String fluentName = null;
		String fluentState = null;
		if(rawFluentName.startsWith("status(")) {
			String[] subParts = CommonUtils.splitInParts(rawFluentName.substring(7,rawFluentName.length()-1));
			if(subParts.length == 2) { //(Fluent,State)
				fluentName = subParts[0];
				fluentState = subParts[1];
			}
		}
		if(fluentName == null) //standard fluent
			fluentName = rawFluentName;
		String groupName = strategy.getGroup(fluentName);
		FluentGroup group = fluentGroups.get(groupName);
		if(group == null) {
			group = new FluentGroup(groupName);
			fluentGroups.put(groupName, group);
		}
		Fluent fluent = group.getFluents().get(fluentName);
		if(fluent == null) {
			fluent = createFluent(fluentName, fluentState != null);
			group.getFluents().put(fluent.getName(), fluent);
		}
		FluentState s = null;
		if(fluentState == null)
			s =  ((StandardFluent)fluent).getState();
		else {
			((MultiValuedFluent)fluent).registerState(fluentState);
			s = ((MultiValuedFluent)fluent).getFluentState(fluentState);
		}
		s.addMVI(mvi);
	}
	
	
	
	private Fluent createFluent(String fluentName, boolean multiValued) {
		if(!multiValued)
			return new StandardFluent(fluentName);
		else
			return new MultiValuedFluent(fluentName);
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("SituationModel: \n");
		for(FluentGroup g: fluentGroups.values()) {
			b.append("-------------------\n");
			b.append(g.toString());
		}
		return b.toString();
	}
	
	
/*
	public void update(FluentsModel updateModel) {
		for(FluentGroup otherGroup: updateModel.getFluentGroups().values()) {
			if(!fluentGroups.containsKey(otherGroup.getName()))
				fluentGroups.put(otherGroup.getName(), otherGroup);
			else {
				FluentGroup myGroup = fluentGroups.get(otherGroup.getName());
				for(Fluent f: otherGroup.getFluents().values()) {
					
				}
			}
		}
	}
*/
	
	public Map<String,FluentGroup> getFluentGroups() {
		return fluentGroups;
	}
	
	
	/*public boolean registerFluent(String fluentName, boolean multiValued) {
		if(fluents.containsKey(fluentName))
			return false;
		else {
			if(!multiValued)
				fluents.put(fluentName, new StandardFluent(fluentName));
			else
				fluents.put(fluentName, new MultiValuedFluent(fluentName));
			return true;
		}
	}
	
	public Map<String,FluentEntity> getFluents() {
		return fluents;
	}
	
	public FluentEntity get(String name) {
		return fluents.get(name);
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("FLUENTS MODEL\n");
		for(String key: fluents.keySet())
			b.append(fluents.get(key));
		return b.toString();
	}
	*/
}
