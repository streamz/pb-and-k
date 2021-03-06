package pbandk.examples.addressbook.pb

data class Person(
    val name: String = "",
    val id: Int = 0,
    val email: String = "",
    val phones: List<pbandk.examples.addressbook.pb.Person.PhoneNumber> = emptyList(),
    val lastUpdated: pbandk.wkt.Timestamp? = null,
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<Person> {
    override operator fun plus(other: Person?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<Person> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = Person.protoUnmarshalImpl(u)
    }

    data class PhoneType(override val value: Int) : pbandk.Message.Enum {
        companion object : pbandk.Message.Enum.Companion<PhoneType> {
            val MOBILE = PhoneType(0)
            val HOME = PhoneType(1)
            val WORK = PhoneType(2)

            override fun fromValue(value: Int) = when (value) {
                0 -> MOBILE
                1 -> HOME
                2 -> WORK
                else -> PhoneType(value)
            }
        }
    }

    data class PhoneNumber(
        val number: String = "",
        val type: pbandk.examples.addressbook.pb.Person.PhoneType = pbandk.examples.addressbook.pb.Person.PhoneType.fromValue(0),
        val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message<PhoneNumber> {
        override operator fun plus(other: PhoneNumber?) = protoMergeImpl(other)
        override val protoSize by lazy { protoSizeImpl() }
        override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
        companion object : pbandk.Message.Companion<PhoneNumber> {
            override fun protoUnmarshal(u: pbandk.Unmarshaller) = PhoneNumber.protoUnmarshalImpl(u)
        }
    }
}

data class AddressBook(
    val people: List<pbandk.examples.addressbook.pb.Person> = emptyList(),
    val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message<AddressBook> {
    override operator fun plus(other: AddressBook?) = protoMergeImpl(other)
    override val protoSize by lazy { protoSizeImpl() }
    override fun protoMarshal(m: pbandk.Marshaller) = protoMarshalImpl(m)
    companion object : pbandk.Message.Companion<AddressBook> {
        override fun protoUnmarshal(u: pbandk.Unmarshaller) = AddressBook.protoUnmarshalImpl(u)
    }
}

private fun Person.protoMergeImpl(plus: Person?): Person = plus?.copy(
    phones = phones + plus.phones,
    lastUpdated = lastUpdated?.plus(plus.lastUpdated) ?: plus.lastUpdated,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Person.protoSizeImpl(): Int {
    var protoSize = 0
    if (name.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(name)
    if (id != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.int32Size(id)
    if (email.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(3) + pbandk.Sizer.stringSize(email)
    if (phones.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(4) + pbandk.Sizer.packedRepeatedSize(phones, pbandk.Sizer::messageSize)
    if (lastUpdated != null) protoSize += pbandk.Sizer.tagSize(5) + pbandk.Sizer.messageSize(lastUpdated)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Person.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (name.isNotEmpty()) protoMarshal.writeTag(10).writeString(name)
    if (id != 0) protoMarshal.writeTag(16).writeInt32(id)
    if (email.isNotEmpty()) protoMarshal.writeTag(26).writeString(email)
    if (phones.isNotEmpty()) protoMarshal.writeTag(34).writePackedRepeated(phones, pbandk.Sizer::messageSize, protoMarshal::writeMessage)
    if (lastUpdated != null) protoMarshal.writeTag(42).writeMessage(lastUpdated)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Person.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Person {
    var name = ""
    var id = 0
    var email = ""
    var phones: pbandk.ListWithSize.Builder<pbandk.examples.addressbook.pb.Person.PhoneNumber>? = null
    var lastUpdated: pbandk.wkt.Timestamp? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Person(name, id, email, pbandk.ListWithSize.Builder.fixed(phones),
            lastUpdated, protoUnmarshal.unknownFields())
        10 -> name = protoUnmarshal.readString()
        16 -> id = protoUnmarshal.readInt32()
        26 -> email = protoUnmarshal.readString()
        34 -> phones = protoUnmarshal.readRepeatedMessage(phones, pbandk.examples.addressbook.pb.Person.PhoneNumber.Companion, false)
        42 -> lastUpdated = protoUnmarshal.readMessage(pbandk.wkt.Timestamp.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun Person.PhoneNumber.protoMergeImpl(plus: Person.PhoneNumber?): Person.PhoneNumber = plus?.copy(
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun Person.PhoneNumber.protoSizeImpl(): Int {
    var protoSize = 0
    if (number.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.stringSize(number)
    if (type.value != 0) protoSize += pbandk.Sizer.tagSize(2) + pbandk.Sizer.enumSize(type)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun Person.PhoneNumber.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (number.isNotEmpty()) protoMarshal.writeTag(10).writeString(number)
    if (type.value != 0) protoMarshal.writeTag(16).writeEnum(type)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun Person.PhoneNumber.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): Person.PhoneNumber {
    var number = ""
    var type: pbandk.examples.addressbook.pb.Person.PhoneType = pbandk.examples.addressbook.pb.Person.PhoneType.fromValue(0)
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return Person.PhoneNumber(number, type, protoUnmarshal.unknownFields())
        10 -> number = protoUnmarshal.readString()
        16 -> type = protoUnmarshal.readEnum(pbandk.examples.addressbook.pb.Person.PhoneType.Companion)
        else -> protoUnmarshal.unknownField()
    }
}

private fun AddressBook.protoMergeImpl(plus: AddressBook?): AddressBook = plus?.copy(
    people = people + plus.people,
    unknownFields = unknownFields + plus.unknownFields
) ?: this

private fun AddressBook.protoSizeImpl(): Int {
    var protoSize = 0
    if (people.isNotEmpty()) protoSize += pbandk.Sizer.tagSize(1) + pbandk.Sizer.packedRepeatedSize(people, pbandk.Sizer::messageSize)
    protoSize += unknownFields.entries.sumBy { it.value.size() }
    return protoSize
}

private fun AddressBook.protoMarshalImpl(protoMarshal: pbandk.Marshaller) {
    if (people.isNotEmpty()) protoMarshal.writeTag(10).writePackedRepeated(people, pbandk.Sizer::messageSize, protoMarshal::writeMessage)
    if (unknownFields.isNotEmpty()) protoMarshal.writeUnknownFields(unknownFields)
}

private fun AddressBook.Companion.protoUnmarshalImpl(protoUnmarshal: pbandk.Unmarshaller): AddressBook {
    var people: pbandk.ListWithSize.Builder<pbandk.examples.addressbook.pb.Person>? = null
    while (true) when (protoUnmarshal.readTag()) {
        0 -> return AddressBook(pbandk.ListWithSize.Builder.fixed(people), protoUnmarshal.unknownFields())
        10 -> people = protoUnmarshal.readRepeatedMessage(people, pbandk.examples.addressbook.pb.Person.Companion, false)
        else -> protoUnmarshal.unknownField()
    }
}
