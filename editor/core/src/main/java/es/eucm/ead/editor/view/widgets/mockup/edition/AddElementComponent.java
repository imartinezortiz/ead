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
package es.eucm.ead.editor.view.widgets.mockup.edition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.editor.AddSceneElementFromResource;
import es.eucm.ead.editor.control.actions.editor.ChangeView;
import es.eucm.ead.editor.view.builders.mockup.camera.Picture;
import es.eucm.ead.editor.view.builders.mockup.edition.EditionWindow;
import es.eucm.ead.editor.view.builders.mockup.edition.SceneEdition;
import es.eucm.ead.editor.view.builders.mockup.gallery.ElementGallery;
import es.eucm.ead.editor.view.builders.mockup.gallery.RepositoryGallery;
import es.eucm.ead.editor.view.listeners.ActionOnClickListener;
import es.eucm.ead.editor.view.widgets.mockup.buttons.BottomProjectMenuButton;
import es.eucm.ead.editor.view.widgets.mockup.buttons.MenuButton.Position;
import es.eucm.ead.editor.view.widgets.mockup.buttons.ToolbarButton;
import es.eucm.ead.editor.view.widgets.mockup.scenes.MockupSceneEditor;
import es.eucm.ead.engine.I18N;

public class AddElementComponent extends EditionComponent {

	private static final String IC_ADD = "ic_addelement",
			IC_PAINT_ELEMENT = "ic_editelement",
			IC_LAST_ELEMENT = "ic_lastelement", IC_REPO_ELEMENT = "ic_cloud",
			IC_GALLERY_ELEMENT = "ic_galleryelement",
			IC_PHOTO_ELEMENT = "ic_photoelement";

	private static final float PREF_BOTTOM_BUTTON_WIDTH = .5F;
	private static final float PREF_BOTTOM_BUTTON_HEIGHT = .2F;

	private EditionToolbar topToolbar;

	public AddElementComponent(final EditionWindow parent,
			Controller controller, Skin skin, Table center,
			MockupSceneEditor scaledView) {
		super(parent, controller, skin);

		this.topToolbar = new EditionToolbar(parent, controller, i18n, skin,
				viewport, center, scaledView);

		Table scrollTable = new Table();
		ScrollPane scroll = new ScrollPane(scrollTable);
		scroll.setScrollingDisabled(true, false);
		add(scroll).fill().expand();
		scrollTable.defaults().uniform();

		Button draw = new BottomProjectMenuButton(viewport,
				i18n.m("edition.tool.add-paint-element"), skin,
				IC_PAINT_ELEMENT, PREF_BOTTOM_BUTTON_WIDTH,
				PREF_BOTTOM_BUTTON_HEIGHT, Position.RIGHT);
		scrollTable.add(draw).fillX().expandX();
		scrollTable.row();

		Button repository;
		scrollTable.add(repository = new BottomProjectMenuButton(viewport, i18n
				.m("edition.tool.add-repository-element"), skin,
				IC_REPO_ELEMENT, PREF_BOTTOM_BUTTON_WIDTH,
				PREF_BOTTOM_BUTTON_HEIGHT, Position.RIGHT));
		repository.addListener(new ActionOnClickListener(controller,
				ChangeView.class, RepositoryGallery.class));
		scrollTable.row();

		scrollTable.add(new BottomProjectMenuButton(viewport, i18n
				.m("edition.tool.add-recent-element"), skin, IC_LAST_ELEMENT,
				PREF_BOTTOM_BUTTON_WIDTH, PREF_BOTTOM_BUTTON_HEIGHT,
				Position.RIGHT));
		scrollTable.row();

		final Button addFromGalleryButton = new BottomProjectMenuButton(
				viewport, i18n.m("edition.tool.add-gallery-element"), skin,
				IC_GALLERY_ELEMENT, PREF_BOTTOM_BUTTON_WIDTH,
				PREF_BOTTOM_BUTTON_HEIGHT, Position.RIGHT);
		addFromGalleryButton.addListener(new ActionOnClickListener(controller,
				ChangeView.class, ElementGallery.class, SceneEdition.class));
		scrollTable.add(addFromGalleryButton).fillX().expandX();
		scrollTable.row();

		final Button addFromSystemGalleryButton = new BottomProjectMenuButton(
				viewport, i18n.m("edition.tool.add-gallery-systemElement"),
				skin, IC_GALLERY_ELEMENT, PREF_BOTTOM_BUTTON_WIDTH,
				PREF_BOTTOM_BUTTON_HEIGHT, Position.RIGHT);
		addFromSystemGalleryButton.addListener(new ActionOnClickListener(
				controller, AddSceneElementFromResource.class));
		scrollTable.add(addFromSystemGalleryButton).fillX().expandX();
		scrollTable.row();

		final Button addFromPhotoButton = new BottomProjectMenuButton(viewport,
				i18n.m("edition.tool.add-photo-element"), skin,
				IC_PHOTO_ELEMENT, PREF_BOTTOM_BUTTON_WIDTH,
				PREF_BOTTOM_BUTTON_HEIGHT, Position.RIGHT);
		addFromPhotoButton.addListener(new ActionOnClickListener(controller,
				ChangeView.class, Picture.class, SceneEdition.class));
		scrollTable.add(addFromPhotoButton).fillX().expandX();

		draw.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!topToolbar.isVisible()) {
					hide();
					topToolbar.setVisible(true);
				} else {
					topToolbar.setVisible(false);
				}
			}
		});
	}

	@Override
	protected Button createButton(Vector2 viewport, Skin skin, I18N i18n) {
		return new ToolbarButton(viewport, skin.getDrawable(IC_ADD),
				i18n.m("edition.add"), skin);
	}

	@Override
	public Array<Actor> getExtras() {
		return null;
	}

	public EditionToolbar getToolbar() {
		return this.topToolbar;
	}

}
