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
package es.eucm.ead.engine.mock;

import ashley.core.Component;
import es.eucm.ead.engine.ComponentLoader;
import es.eucm.ead.engine.EntitiesLoader;
import es.eucm.ead.engine.GameLoop;
import es.eucm.ead.engine.assets.GameAssets;
import es.eucm.ead.engine.mock.schema.MockModelComponent;
import es.eucm.ead.engine.processors.ComponentProcessor;

/**
 * Created by angel on 5/05/14.
 */
public class MockEntitiesLoader extends EntitiesLoader {

	static GameAssets mockGameAssets;

	static ComponentLoader mockComponentLoader;

	static GameAssets getMockGameAssets() {
		mockGameAssets = new GameAssets(new MockFiles());
		return mockGameAssets;
	}

	static ComponentLoader getMockComponentLoader() {
		mockComponentLoader = new ComponentLoader(mockGameAssets);
		return mockComponentLoader;
	}

	public MockEntitiesLoader() {
		super(getMockGameAssets(), getMockComponentLoader(), new GameLoop());
		gameAssets.addClassTag("mock", MockModelComponent.class);
		componentLoader.registerComponentProcessor(MockModelComponent.class,
				new ComponentProcessor<MockModelComponent>(gameLoop) {
					@Override
					public Component getComponent(
							MockModelComponent modelComponent) {
						MockEngineComponent component = new MockEngineComponent();
						component.setFloatAttribute(modelComponent
								.getFloatAttribute());
						return component;
					}
				});
	}

	public GameLoop getGameLoop() {
		return gameLoop;
	}
}
