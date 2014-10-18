package br.unb.unbomber.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Simple implementation of a Entity Manager. 
 * 
 * @author grodrigues
 *
 */
public class EntitySystemImpl implements EntityManager {

	private Map<Class<?>, List<Event>> events;
	
	private Map<Class<?>, List<Component>> components;
	
	private static EntityManager instance;
	
	private int uniqueIdSequence;
	
	public static void init(){
		instance = new EntitySystemImpl(
				new HashMap<Class<?>, List<Event>>(),
				new HashMap<Class<?>, List<Component>>());
	}
	
	private EntitySystemImpl(Map<Class<?>, List<Event>> events, Map<Class<?>,List<Component>> components){
		this.events = events;
		this.components = components;
	}
	
	public static EntityManager getInstance(){
		if(instance==null){
			init();
		}
		return instance;
	}
	
	@Override
	public List<Event> getEvents(Class<?> type) {
		List<Event> result = events.get(type);
		return result;
	}

	@Override
	public List<Component> getComponents(Class<?> componentType) {
		return this.components.get(componentType);
	}

	@Override
	public Component getComponent(Class<?> componentType,
			int entityId) {
		List<Component> componentsOfType = this.components.get(componentType);
		
		//Should it throw an Exception??
		if(componentsOfType==null){
			return null;
		}
		
		for(Component component: componentsOfType){
			//return the first with the required id
			if(component.getEntityId() == entityId){
				return component;
			}
		}
		//if none return null
		return null;
	}

	@Override
	public void addEvent(Event event) {
		List<Event> eventList = this.events.get(event.getClass());
		if(eventList == null){
			eventList = new ArrayList<Event>();
			this.events.put(event.getClass(), eventList);
		}
		eventList.add(event);
	}

	@Override
	public void addComponent(Component component) {
		List<Component> componentList = this.components.get(component.getClass());
		if(componentList == null){
			componentList = new ArrayList<Component>();
			this.components.put(component.getClass(), componentList);
		}
		componentList.add(component);
	}

	@Override
	public void addEntity(Entity entity) {
		entity.setEntityId(getUniqueId());
		for(Component component:entity.getComponents()){
			addComponent(component);
		}
	}

	/**
	 * Return a Unique Id each time it's called
	 * 
	 * @return uniqueId
	 */
	public int getUniqueId(){
		return this.uniqueIdSequence++;
	}

	@Override
	public void remove(Component component) {
		List<Component> componentList = this.components.get(component.getClass());
		if(componentList == null){
			throw new IllegalArgumentException("Type not in entity manager. It was not added so it can't be removed");
		}
		componentList.remove(component);
	}

	@Override
	public void remove(Entity entity) {
		for(Component component:entity.getComponents()){
			remove(component);
		}
	}
	
	@Override
	public void removeEntityById(int entityId) {
		for(Class<?> type:components.keySet()){
			removeComponentByEntityId(type, entityId);
		}
	}
	
	@Override
	public void removeComponentByEntityId(Class<?> componentType, int entityId) {
		List<Component> componentsOfType = this.components.get(componentType);
		
		//Should it throw an Exception??
		if(componentsOfType==null){
			return;
		}
		
		for(Component component: componentsOfType){
			//remove if it has the expected entityId
			if(component.getEntityId() == entityId){
				componentsOfType.remove(component);
			}
		}		
	}

	@Override
	public void remove(Event event) {
		List<Event> eventList = this.events.get(event.getClass());
		if(eventList == null){
			throw new IllegalArgumentException("Type not in entity manager. It was not added so it can't be removed");
		}
		eventList.remove(event);		
	}

}
