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
package es.eucm.ead.editor.ui.maintoolbar;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.Action;
import es.eucm.ead.editor.control.actions.EditorAction;
import es.eucm.ead.editor.control.actions.editor.Back;
import es.eucm.ead.editor.control.actions.editor.Copy;
import es.eucm.ead.editor.control.actions.editor.Cut;
import es.eucm.ead.editor.control.actions.editor.Exit;
import es.eucm.ead.editor.control.actions.editor.Next;
import es.eucm.ead.editor.control.actions.editor.NewGame;
import es.eucm.ead.editor.control.actions.editor.OpenGame;
import es.eucm.ead.editor.control.actions.editor.Paste;
import es.eucm.ead.editor.control.actions.editor.Redo;
import es.eucm.ead.editor.control.actions.editor.Save;
import es.eucm.ead.editor.control.actions.editor.ShowContextMenu;
import es.eucm.ead.editor.control.actions.editor.Undo;
import es.eucm.ead.editor.view.listeners.ActionOnClickListener;
import es.eucm.ead.editor.view.listeners.ActionOnDownListener;
import es.eucm.ead.editor.view.widgets.IconButton;
import es.eucm.ead.editor.view.widgets.Separator;
import es.eucm.ead.editor.view.widgets.layouts.LinearLayout;
import es.eucm.ead.editor.view.widgets.menu.ContextMenu;
import es.eucm.ead.engine.I18N;

/**
 * Main toolbar, with the main menu and global tools: save, cut, copy, paste,
 * undo, redo, search...
 */
public class MainToolbar extends LinearLayout {

	public static int IMAGE_PADDING = 5;

	private Controller controller;

	public MainToolbar(Controller controller) {
		super(true);
		this.controller = controller;

		I18N i18N = controller.getApplicationAssets().getI18N();
		Skin skin = controller.getApplicationAssets().getSkin();

		IconButton eAdventureButton = new IconButton("logomenu64x64",
				IMAGE_PADDING, skin);
		eAdventureButton.addListener(new ActionOnClickListener(controller,
				ShowContextMenu.class, eAdventureButton, buildFileMenu(skin,
						i18N)));
		add(eAdventureButton).expand(true, true);

		LinearLayout controlsTop = new LinearLayout(true);
		controlsTop.add(createIcon("back24x24", skin, Back.class,
				i18N.m("maintoolbar.back.tooltip")));
		controlsTop.add(createIcon("forward24x24", skin, Next.class,
				i18N.m("maintoolbar.next.tooltip")));

		TextField searchTextField = new TextField("", skin);
		searchTextField.setMessageText(i18N.m("general.search"));

		controlsTop.add(searchTextField).margin(5, 0, 0, 0).expandX();

		LinearLayout controlsBottom = new LinearLayout(true);
		controlsBottom.add(createIcon("save24x24", skin, Save.class,
				i18N.m("maintoolbar.save.tooltip")));
		controlsBottom.add(new Separator(false, skin));
		controlsBottom.add(createIcon("cut24x24", skin, Cut.class,
				i18N.m("maintoolbar.cut.tooltip")));
		controlsBottom.add(createIcon("copy24x24", skin, Copy.class,
				i18N.m("maintoolbar.copy.tooltip")));
		controlsBottom.add(createIcon("paste24x24", skin, Paste.class,
				i18N.m("maintoolbar.paste.tooltip")));
		controlsBottom.add(new Separator(false, skin));
		controlsBottom.add(createIcon("undo24x24", skin, Undo.class,
				i18N.m("maintoolbar.undo.tooltip")));
		controlsBottom.add(createIcon("redo24x24", skin, Redo.class,
				i18N.m("maintoolbar.redo.tooltip")));

		LinearLayout container = new LinearLayout(false).pad(5)
				.defaultWidgetsMargin(0);
		container.add(controlsTop).left();
		container.add(controlsBottom).left();

		add(container).centerY();
	}

	/**
	 * 
	 * @return a button with the given drawable, that, when clicked executes the
	 *         given {@link EditorAction}
	 */
	private <T extends EditorAction> IconButton createIcon(String drawable,
			Skin skin, Class<T> editorAction, String tooltip) {
		IconButton iconButton = new IconButton(drawable, IMAGE_PADDING, skin);
		iconButton.addListener(new ActionOnClickListener(controller,
				editorAction));
		iconButton.setTooltip(tooltip);
		return iconButton;
	}

	private ContextMenu buildFileMenu(Skin skin, I18N i18N) {
		ContextMenu contextMenu = new ContextMenu(skin);
		item(contextMenu, i18N.m("general.new"), NewGame.class);
		item(contextMenu, i18N.m("general.open"), OpenGame.class);
		contextMenu.separator();
		item(contextMenu, i18N.m("general.save"), Save.class);
		contextMenu.separator();
		contextMenu.item(i18N.m("file.recents")).submenu(
				new RecentsMenu(skin, controller, i18N));
		contextMenu.separator();
		item(contextMenu, i18N.m("file.exit"), Exit.class);
		return contextMenu;
	}

	private <T extends Action> ContextMenu item(ContextMenu contextMenu,
			String label, Class<T> action, Object... args) {
		contextMenu.item(label).addListener(
				new ActionOnDownListener(controller, action, args));
		return contextMenu;
	}

}
