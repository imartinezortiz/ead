/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2013 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
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
package es.eucm.ead.editor.view.generic;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import es.eucm.ead.editor.control.Command;
import es.eucm.ead.editor.control.commands.ChangeFieldCommand;
import es.eucm.ead.editor.model.DependencyNode;

public class IntegerOption extends AbstractOption<Integer> {

    private int min;
    private int max;
    protected TextField textField;   
    protected int minWidth = 50;
	
    /**
	 * A number option for integers from min (included) to max (excluded)
	 * @param title
	 * @param toolTipText
	 * @param nodes
	 */
	public IntegerOption(String title, String toolTipText, DependencyNode ...nodes) {
		super(title, toolTipText, nodes);
	}

    /**
     * @param min value (inclusive) for this control 
     * @return the configured IntegerOption
     */
    public IntegerOption min(int min) {
        this.min = min;
        return this;
    }

    /**
     * @param max value (inclusive) for this control 
     * @return the configured IntegerOption
     */
    public IntegerOption max(int max) {
        this.max = max;
        return this;
    }
    
    @Override
	public Integer getControlValue() {
		String text = textField.getText();
        try {
            Integer i = Integer.parseInt(text);
            if (i >= min && i <= max) {
                return i;
            }
        } catch (NumberFormatException nfe) {
            // do nothing, will overwrite
        }
        return accessor.read();
	}

	@Override
	public void setControlValue(Integer newValue) {
        textField.setText(newValue.toString());
	}

	@Override
	public WidgetGroup createControl() {
        textField = new TextField("", skin);
		textField.setText("" + accessor.read());
		textField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
				update();
                return true;
            }
        });
        WidgetGroup wg = new WidgetGroup() {
            @Override
            public float getMinWidth() {
                return minWidth;
            }            
        };
        wg.addActor(textField);
        return wg;
	}

	@Override
	protected Command createUpdateCommand() {
		// Users expect to undo/redo entire words, rather than character-by-character
		return new ChangeFieldCommand<Integer>(getControlValue(), accessor,
				changed) {
			@Override
			public boolean likesToCombine(Integer nextValue) {
				// return Math.abs(nextValue - oldValue) <= 1;
				return true;
			}
		};
	}
}
