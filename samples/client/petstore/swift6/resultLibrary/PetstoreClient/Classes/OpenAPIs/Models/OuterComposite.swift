//
// OuterComposite.swift
//
// Generated by openapi-generator
// https://openapi-generator.tech
//

import Foundation

internal struct OuterComposite: Sendable, Codable, ParameterConvertible {

    internal private(set) var myNumber: Double?
    internal private(set) var myString: String?
    internal private(set) var myBoolean: Bool?

    internal init(myNumber: Double? = nil, myString: String? = nil, myBoolean: Bool? = nil) {
        self.myNumber = myNumber
        self.myString = myString
        self.myBoolean = myBoolean
    }

    internal enum CodingKeys: String, CodingKey, CaseIterable {
        case myNumber = "my_number"
        case myString = "my_string"
        case myBoolean = "my_boolean"
    }

    // Encodable protocol methods

    internal func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(myNumber, forKey: .myNumber)
        try container.encodeIfPresent(myString, forKey: .myString)
        try container.encodeIfPresent(myBoolean, forKey: .myBoolean)
    }
}

