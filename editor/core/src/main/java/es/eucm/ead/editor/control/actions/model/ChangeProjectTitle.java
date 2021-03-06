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
package es.eucm.ead.editor.control.actions.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import es.eucm.ead.editor.assets.EditorGameAssets;
import es.eucm.ead.editor.control.Preferences;
import es.eucm.ead.editor.control.actions.ModelAction;
import es.eucm.ead.editor.control.actions.editor.Save;
import es.eucm.ead.editor.control.commands.Command;
import es.eucm.ead.editor.control.commands.FieldCommand;
import es.eucm.ead.schemax.FieldNames;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.schema.editor.components.Note;
import es.eucm.ead.schema.entities.ModelEntity;

import java.io.File;

public class ChangeProjectTitle extends ModelAction {

	private static final String PROJECT_TITLE_FIELD = "Project title";

	public ChangeProjectTitle() {
		// FIXME I think this class needs renaming (ProjectTitle)
	}

	@Override
	public Command perform(Object... args) {
		ModelEntity currProj = controller.getModel().getGame();
		Note note = Model.getComponent(currProj, Note.class);
		final String oldTitle = note.getTitle();
		final String newTitle = args[0].toString();
		if (newTitle.equals(oldTitle)) {
			Gdx.app.log(PROJECT_TITLE_FIELD, "Old title equals new title!");
			return null;
		}

		final Command changeTitleCom = new FieldCommand(note,
				FieldNames.NOTE_TITLE, newTitle, true);
		controller.command(changeTitleCom);
		controller.action(Save.class);

		final EditorGameAssets editorGameAssets = controller
				.getEditorGameAssets();
		final String oldProjPath = editorGameAssets.getLoadingPath();
		final FileHandle projectDir = editorGameAssets.absolute(oldProjPath);
		if (!projectDir.exists()) {
			Gdx.app.error(PROJECT_TITLE_FIELD, "Project path doesn't exist!");
			return null;
		}
		if (!projectDir.isDirectory()) {
			Gdx.app.error(PROJECT_TITLE_FIELD,
					"Project path isn't a directory!");
			return null;
		}

		final String projectDirName = projectDir.name();
		if (projectDirName.equals(newTitle)) {
			Gdx.app.error(PROJECT_TITLE_FIELD,
					"Project's folder has the same title!");
			return null;
		}
		final FileHandle parentDir = projectDir.parent();
		for (FileHandle child : parentDir.list()) {
			if (child != projectDir && child.isDirectory()
					&& child.name().equals(newTitle)) {
				Gdx.app.error(PROJECT_TITLE_FIELD,
						"There is another project with the same title!");
				return null;
			}
		}
		final String newPath = controller.getEditorGameAssets()
				.toCanonicalPath(
						parentDir.file().getAbsolutePath() + File.separator
								+ newTitle + File.separator);
		projectDir.moveTo(editorGameAssets.absolute(newPath));
		editorGameAssets.setLoadingPath(newPath, false);
		// FIXME Shouldn't there be a method like
		// Preferences#addRecent ?
		final Preferences prefs = controller.getPreferences();
		prefs.putString(Preferences.RECENT_GAMES,
				newPath + ";" + prefs.getString(Preferences.RECENT_GAMES, ""));
		Gdx.app.log(PROJECT_TITLE_FIELD,
				"Project renamed and preferences updated!");
		return null;
	}

	interface TitleChangedListener {

		/**
		 * Invoked when the title was changed.
		 */
		void onTitleChanged();
	}
}
