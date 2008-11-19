package storymaps;

import DragAndDrop.*;
import java.util.HashSet;

/**
 * The collection of story cards that the user can drag from.
 * 
 * @author seanh
 */
public class StoryMap extends StoryBase
                      implements DroppableOwner, DragDropObserver {
    
    private HashSet<StoryCard> storycards = new HashSet<StoryCard>();
    
    public StoryMap(String title_text) {
        super(title_text);
        
        try {
            Droppable d = new Droppable(background,this);            
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }        
    }
    
    public boolean dropped_onto(DropEvent de) {
        StoryCard s = (StoryCard) de.getDraggee().getNode().getAttribute("StoryCard");
        if (s == null) { return false; }
        add(s.getNode());
        storycards.add(s);
        de.getDraggee().attach(this);
        return true;
    }
    
    public boolean notify(DropEvent de) {
        return false;
    }   
}