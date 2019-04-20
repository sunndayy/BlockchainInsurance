// singleton
class BlockTree {
    private:
        dictionary(blockHash => blockHeader => blockData): _blockDict;
        [BlockHeader]: _mainChain
        State: _state

}