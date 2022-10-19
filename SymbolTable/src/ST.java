
import java.util.*;

// 1a for next lab

public class ST {
    private Integer nrElements;
    private Integer size;
    private List<Object>[] elements;

    public ST(Integer size) {
        this.nrElements = 0;
        this.size = size;
        this.elements = new ArrayList[this.size];
        for(var i = 0; i < this.size; i ++){
            this.elements[i] = new ArrayList<>();
        }
    }

    public List<Object>[] getElements() {
        return elements;
    }

    private int hashFunction(Object element) {
        Integer f = -1;
        if(element instanceof String) {
             f = ((String)element).chars().reduce(Integer::sum).orElseThrow();
        }else if(element instanceof Integer){
            f = (((Integer)element).toString()).chars().reduce(Integer::sum).orElseThrow();
        }
        return f % this.size;
    }

    private void resize() {
        this.size = this.size * 2;
        var newElements = new ArrayList[this.size];
        for(var i = 0; i < this.size; i ++){
            newElements[i] = new ArrayList<>();
        }
        this.nrElements = 0;
        for(var list: this.elements){
            for(var element: list){
                var bucket = this.hashFunction(element);
                newElements[bucket].add(element);
                this.nrElements +=1 ;
            }
        }
        this.elements = newElements;
    }

    private AbstractMap.SimpleEntry<Integer, Integer> getIdentifierPosition(Object element) {
        var position = new AbstractMap.SimpleEntry<>(-1,-1);
        var bucket = this.hashFunction(element);
        for(var i = 0; i < this.elements[bucket].size(); i ++){
            var el = this.elements[bucket].get(i);
            if(el.equals(element)){
                position = new AbstractMap.SimpleEntry<>(bucket, i);
            }
        }
        return position;
    }

    public AbstractMap.SimpleEntry<Integer, Integer> addElement(Object element) {
        AbstractMap.SimpleEntry<Integer, Integer> existingPosition = getIdentifierPosition(element);
        if(existingPosition.equals(new AbstractMap.SimpleEntry<>(-1,-1))){
            if(this.nrElements > this.size * 2 ){
                this.resize();
            }
            var bucket = this.hashFunction(element);
            var posInBucket = this.elements[bucket].size();
            this.elements[bucket].add(element);
            existingPosition = new AbstractMap.SimpleEntry<>(bucket, posInBucket);
            this.nrElements +=1 ;
        }
        return existingPosition;
    }
}
