package br.unb.unbomber.match;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.unb.entitysystem.EntityManager;
import br.unb.entitysystem.EntityManagerImpl;
import br.unb.entitysystem.System;
import br.unb.unbomber.systems.BombSystem;
import br.unb.unbomber.systems.CollisionSystem;
import br.unb.unbomber.systems.ExplosionSystem;
import br.unb.unbomber.systems.LifeSystem;
import br.unb.unbomber.systems.MovementSystem2;
import br.unb.unbomber.systems.TimeSystem;

public class GameMatch {

	
	/** The system. */
	List<System> systems;

	/** The entity manager. */
	EntityManager entityManager;
	
	private final static Logger LOGGER = Logger.getLogger(GameMatch.class.getName()); 
	
	
	/**
	 * Create the Base Systes for the match.
	 * 
	 */
	public GameMatch() {
		
		//init a new system for each test case
		EntityManagerImpl.init();
		entityManager = EntityManagerImpl.getInstance();
		
		systems = new ArrayList<System>();

		systems.add(new TimeSystem(entityManager));

		systems.add(new BombSystem(entityManager));
		systems.add(new CollisionSystem(entityManager));
		systems.add(new ExplosionSystem(entityManager));
		systems.add(new LifeSystem(entityManager));
		systems.add(new MovementSystem2(entityManager));
	}
	
	
	
	
	
	/**
	 * Include a new System.
	 * 
	 * Used for UI projects to include Render, Sound and Control
	 * Systems.
	 * 
	 * @param system
	 */
	public void addSystem(System system){
		this.systems.add(system);
	}

	public EntityManager getEntityManager(){
		return this.entityManager;
	}
	
	public void start() {
		
		if(this.systems == null){
			return;
		}
		
		for(System system:this.systems){
			try{
				system.start();
			/** Log the system errors and continue*/
			}catch(Exception e){
				log("Not expected error in " + system.getClass().getName(), e);
			}
		}
	}
		
	public void update() {
			for(System system:this.systems){
				try{
					system.update();
				/** Log the system errors and continue*/
				}catch(Exception e){
					log("Not expected error in " + system.getClass().getName(), e);
				}
		}
	}
	
	public void log(String msg,Throwable thrown){
		LOGGER.log(Level.SEVERE, msg, thrown);
	}

}
