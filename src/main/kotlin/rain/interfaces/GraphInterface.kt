package rain.interfaces

interface GraphInterface {

    // NOTE: this was a dunder method in python implementation
    fun contains(key: String): Boolean

    // NOTE: this was named exists in python implementation
    fun contains(item:GraphableItem): Boolean

    fun create(item:GraphableItem)

    fun merge(item:GraphableItem)

    fun read(item:GraphableItem)

    // TODO: assume not needed
//    fun readRelationship(item:GraphableItem)

    fun save(item:GraphableItem)

    fun delete(key: String)

    // TODO maybe: could this be a val?
//    fun selectItems(select: SelectInterface):Sequence<LanguageItem>

    fun <T: LanguageItem>selectItems(select: SelectInterface):Sequence<T>

}

//@abstractmethod
//def get_typed(self, key:str, context:"ContextInterface") -> GraphableInterface: pass
//
//@abstractmethod
//def select_interface(self, select:SelectInterface): pass #TODO - return type hint?
