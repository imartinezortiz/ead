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
package es.eucm.ead.editor.control.actions.editor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import es.eucm.ead.editor.control.actions.EditorAction;
import es.eucm.ead.editor.view.widgets.menu.ContextMenu;

/**
 * <p>
 * Shows a context menu besides a given actor
 * </p>
 * <dl>
 * <dt><strong>Arguments</strong></dt>
 * <dd><strong>args[0]</strong> <em>Actor</em> The reference actor to show the
 * context menu</dd>
 * <dd><strong>args[1]</strong> <em>ContextMenu</em> the context menu to show</dd>
 * </dl>
 */
public class ShowContextMenu extends EditorAction {

	private Vector2 origin = new Vector2();

	public ShowContextMenu() {
		super(true, false, Actor.class, ContextMenu.class);
	}

	@Override
	public void perform(Object... args) {
		Actor actor = (Actor) args[0];
		ContextMenu contextMenu = (ContextMenu) args[1];

		origin.set(0, 0);
		actor.localToStageCoordinates(origin);
		controller.getViews().showContextMenu(contextMenu, origin.x, origin.y);

	}
}
