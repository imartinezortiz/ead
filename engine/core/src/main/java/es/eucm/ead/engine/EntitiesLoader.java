/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.engine;

import ashley.core.Component;
import ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import es.eucm.ead.engine.assets.Assets.AssetLoadedCallback;
import es.eucm.ead.engine.assets.GameAssets;
import es.eucm.ead.engine.components.MultiComponent;
import es.eucm.ead.engine.components.TouchedComponent;
import es.eucm.ead.engine.components.behaviors.TouchesComponent;
import es.eucm.ead.engine.components.controls.ControlComponent;
import es.eucm.ead.engine.components.renderers.RendererComponent;
import es.eucm.ead.engine.entities.EngineEntity;
import es.eucm.ead.engine.entities.actors.RendererActor;

import es.eucm.ead.schema.components.ModelComponent;
import es.eucm.ead.schema.entities.ModelEntity;

/**
 * Converts {@link ModelEntity} into ashley {@link Entity}s. Delegates in
 * {@link ComponentLoader} to transform model components into engine components.
 */
public class EntitiesLoader implements AssetLoadedCallback<ModelEntity> {

	protected GameAssets gameAssets;

	protected ComponentLoader componentLoader;

	protected GameLoop gameLoop;

	private final RenderActorListener renderActorListener = new RenderActorListener();

	private ObjectMap<String, EntityLoadedCallback> loading;

	public EntitiesLoader(GameAssets gameAssets,
			ComponentLoader componentLoader, GameLoop gameLoop) {
		this.gameAssets = gameAssets;
		this.gameLoop = gameLoop;
		loading = new ObjectMap<String, EntityLoadedCallback>();
		this.componentLoader = componentLoader;
	}

	/**
	 * Returns the {@code ComponentLoader} used to create engine components,
	 * resolve model component alias, etc.
	 */
	public ComponentLoader getComponentLoader() {
		return componentLoader;
	}

	/**
	 * Starts loading the model entity stored in the given {@code path}. Load is
	 * asynchronous so a {@code callback} must be passed to receive the
	 * notification when the ModelEntity is ready.
	 * 
	 * @param path
	 *            a relative path inside the game resources containing an entity
	 *            (e.g. "scenes/scene2.json")
	 * @param callback
	 *            The callback that is notified when the process is complete
	 */
	public void loadEntity(String path, EntityLoadedCallback callback) {
		loading.put(path, callback);
		gameAssets.get(path, ModelEntity.class, this);
	}

	/**
	 * Just completes loading of any entities pending
	 */
	public void finishLoading() {
		gameAssets.finishLoading();
	}

	@Override
	// This method gets invoked when an entity scheduled for loading is ready.
	// It just notifies the associated callback.
	public void loaded(String fileName, ModelEntity asset) {
		EntityLoadedCallback callback = loading.remove(fileName);
		callback.loaded(fileName, toEngineEntity(asset));
	}

	/**
	 * This method converts the given {@code modelEntity} {@link ModelEntity}
	 * into a fully functional runtime entity ({@link EngineEntity}). It also
	 * creates any {@link Component} needed and attaches it to the recently
	 * created entity. This method works recursively on all children.
	 * 
	 * Note: this method does not actually attach the entity to LibGdx's scene
	 * tree, so it won't be rendered on the screen unless that is done
	 * separately. This method adds the EngineEntity created to the game engine,
	 * so it will be processed by any system that needs to.
	 * 
	 * @param modelEntity
	 *            The {@link ModelEntity} to be transformed into an
	 *            {@link Entity}.
	 * @return The entity added
	 */
	public EngineEntity toEngineEntity(ModelEntity modelEntity) {
		EngineEntity entity = gameLoop.createEntity();
		entity.setModelEntity(modelEntity);

		for (ModelComponent component : modelEntity.getComponents()) {
			addComponent(entity, componentLoader.getComponent(component));
		}
		gameLoop.addEntity(entity);

		for (ModelEntity c : modelEntity.getChildren()) {
			entity.getGroup().addActor(toEngineEntity(c).getGroup());
		}

		return entity;
	}

	private void addComponent(EngineEntity entity, Component c) {
		if (c != null) {
			if (c instanceof MultiComponent) {
				for (Component component : ((MultiComponent) c).getComponents()) {
					addComponent(entity, component);
				}
			} else {
				entity.add(c);
				// Special cases
				if (c instanceof RendererComponent) {
					RendererComponent rendererComponent = (RendererComponent) c;
					RendererActor renderer = Pools.obtain(RendererActor.class);
					renderer.setRenderer(rendererComponent);
					renderer.addListener(renderActorListener);
					Group group = entity.getGroup();
					group.addActor(renderer);
					group.setSize(renderer.getWidth(), renderer.getHeight());
					entity.add(rendererComponent);
				}

				if (c instanceof ControlComponent) {
					Actor control = ((ControlComponent) c).getControl();
					control.addListener(renderActorListener);
					entity.getGroup().addActor(control);
				}

				if (c instanceof TouchesComponent) {
					entity.getGroup().addListener(renderActorListener);
				}
			}
		}
	}

	private class RenderActorListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			Actor listenerActor = event.getListenerActor();
			while (listenerActor != null) {
				Object o = listenerActor.getUserObject();
				if (o instanceof Entity) {
					Entity entity = (Entity) o;
					if (entity.hasComponent(TouchesComponent.class)) {
						TouchedComponent component = gameLoop
								.createComponent(TouchedComponent.class);
						component.touch();
						entity.add(component);
						return;
					}
				}
				listenerActor = listenerActor.getParent();
			}
		}
	}

	/**
	 * Simple callback that gets notified when an entity scheduled for loading
	 * from disk is ready
	 */
	public interface EntityLoadedCallback {
		/**
		 * ModelEntity loaded successfully.
		 * 
		 * @param path
		 *            The relative path of the entity
		 * @param engineEntity
		 *            The runtime entity created.
		 */
		public void loaded(String path, EngineEntity engineEntity);
	}
}
